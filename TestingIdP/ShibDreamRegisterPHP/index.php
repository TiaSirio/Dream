<?php
use MatMacSystem\Route;
include 'Route.php';


define('BASEPATH', '/');

session_start();

Route::get("/", function(){
   echo "<h1>auth.dreamplatform.it - Node 2 (Auth node)</h1>";
});

//Main page
Route::get("/register", function(){
include 'views/register.php';
});

Route::get("/confirm", function(){
if (isset($_GET['confirmationCode'])){
    include 'controller/confirmationController.php';
} else {
    include "views/confirm.php";
}
});

Route::post("/register/post", function (){
    include 'controller/registerPost.php';
});

Route::get("/reset", function (){
    include 'views/passwordRequest.php';
});

Route::post("/reset/post", function (){
    include 'controller/resetController.php';
});

Route::get("/reset/confirm", function (){
    if (isset($_GET['resetCode'])){
        include 'controller/resetController.php';
    } else {
        include "views/passwordConfirm.php";
    }
});

Route::methodNotAllowed(function ($path, $method) {
    header('HTTP/1.0 405 Method Not Allowed');
    echo "<h1>405 - Method not allowed</h1>";
});

Route::pathNotFound(function () {
    header('HTTP/1.0 404 Not Found');
    echo "<h1>404 - Not found</h1>";
});

Route::run(BASEPATH);
