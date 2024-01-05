<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>Dream IdP</title>

    <link href="/css/styles.css" rel="stylesheet" />
    <link href="/css/login.css" rel="stylesheet" />

</head>

<body class="text-center">
<form class="form-signin" style="margin-top: -15px" >
    <img src="/assets/auth_logo.png" class="col-12 mb-3" alt="Logo"/>
    <?php
    if (isset($_GET['evn_success'])){
        echo '<div class="alert alert-success">Password successfully changed</div>';
    } else if (isset($_GET['evn_error'])) {
        echo '<div class="alert alert-danger">An error occurred, please try again</div>';
    } else {
        echo '<p>Request completed.<br/>
        A mail has been sent to your email address with the instruction to reset the password</p>';
    }
    ?>
</form>

<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script><!-- jQuery Custom Scroller CDN -->

</body>

</html>
