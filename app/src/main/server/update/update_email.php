<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$email = $_POST["email"];

$query = "UPDATE business SET email='$email' WHERE id='$businessId'";
mysqli_query($link, $query);
?>