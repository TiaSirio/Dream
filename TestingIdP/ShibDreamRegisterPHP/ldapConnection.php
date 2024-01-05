<?php
$ldap=array (
    'host' =>'192.168.1.121',
    'port' => '3389',
    'base_dn' => 'dc=dreamplatform,dc=it',
    'bind_rdn' => 'cn=admin,dc=dreamplatform,dc=it',
    'bind_pw'  => 'MatMacsystem1998',
);

$ldapconn = ldap_connect($ldap['host'], $ldap['port']);

if($ldapconn===false) {
    die('can\'t connect to ldap');
}


ldap_set_option($ldapconn, LDAP_OPT_PROTOCOL_VERSION, 3);
$bind = ldap_bind($ldapconn,$ldap['bind_rdn'], $ldap['bind_pw']);


if($bind===false) {
    die('can\'t bind to ldap: '.ldap_error ($ldapconn));
}