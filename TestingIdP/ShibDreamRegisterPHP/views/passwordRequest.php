<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>Dream IdP</title>

    <link href="css/styles.css" rel="stylesheet" />
    <link href="css/login.css" rel="stylesheet" />

</head>

<body class="text-center">
<form class="form-signin" style="margin-top: -15px" method="post" action="/reset/post">
    <img src="assets/auth_logo.png" class="col-12 mb-3" alt="Logo"/>
    <div class="row">
        <?php
        if (isset($_GET['error'])){
            echo '<div class="col-12"><div class="alert alert-danger">Invalid email address</div></div>';
        }
        ?>
        <div class="col-12">
            <input class="form-control" placeholder="Email address" required="" name="email" type="email"/>
        </div>
        <div class="col-12">
            <input type="submit" name="evn_change" value="Change" class="btn btn-primary float-end"/>
        </div>
    </div>
</form>

<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script><!-- jQuery Custom Scroller CDN -->

</body>

</html>
