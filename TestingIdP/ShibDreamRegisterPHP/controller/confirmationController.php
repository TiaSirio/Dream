<?php
include "ldapConnection.php";

if(!isset($_GET['confirmationCode']) || empty($_GET['confirmationCode'])){
    header("Location: /confirm?evn_error");
    exit();
}
$token = $_GET['confirmationCode'];
//CHECK AUTHCODE AND GET DATAA FROM DB
$db = new \mysqli("192.168.1.121", "se2group", "se2projectpassword", "dream_shibd");
$sql = "SELECT * FROM pending_user WHERE confirmationCode=?";
$stmt = $db->prepare($sql);
$stmt->bind_param("s", $token);
$stmt->execute();
$result = $stmt->get_result();
$stmt->close();
if($result->num_rows == 0){
    header("Location: /confirm?evn_error");
    exit();
}
$row = $result->fetch_assoc();
$name = $row['name'];
$surname = $row['surname'];
$mail = $row['mail'];
$birthdate = $row['birthdate'];
$areaOfResidence = $row['areaOfResidence'];
$password = dec_password($row['password']);
$policyMakerID = $row['policyMakerID'];

//INSERT USER IN LDAP
$info=array();
$info["objectClass"] =array();
$info["objectClass"][] = "top";
$info["objectClass"][] = "person";
$info["objectClass"][] = "organizationalPerson";
$info["objectClass"][] = "inetOrgPerson";
$info["objectClass"][] = "dreamPerson";
$info["objectClass"][] = "simpleSecurityObject";

$info["uid"]=$mail;
$info["mail"]=$mail;
$info["givenName"]=$name;
$info["displayName"]=$name." ".$surname;
$info["dateOfBirth"]=$birthdate;
$info["areaOfResidence"]=$areaOfResidence;
$info["sn"]=$surname;
$info["cn"]=$name.".".$surname;
$info["userPassword"]=ldapPassword($password);
if(!is_null($policyMakerID)){
    $info["policyMakerID"]=$policyMakerID;
}



if (ldap_add($ldapconn, "uid=$mail,ou=Users,dc=dreamplatform,dc=it", $info)){
    $sql = "DELETE FROM pending_user WHERE confirmationCode=?";
    $stmt = $db->prepare($sql);
    $stmt->bind_param("s", $token);
    $stmt->execute();
    $stmt->close();
    header("Location: /confirm?evn_success");
} else {
    header("Location: /confirm?evn_error");
}



function dec_password($hash){
    $ciphering = "AES-128-CTR";
    $options = 0;
    $decryption_iv = '1234567891011121';
    $decryption_key = "DreamShibbolethUserPassword";
    return openssl_decrypt ($hash, $ciphering,
        $decryption_key, $options, $decryption_iv);
}

function ldapPassword($password){
    mt_srand((double)microtime()*1000000);
    $salt = pack("CCCC", mt_rand(), mt_rand(), mt_rand(), mt_rand());
    $hash = "{SSHA}" . base64_encode(pack("H*", sha1($password . $salt)) . $salt);
    return $hash;
}