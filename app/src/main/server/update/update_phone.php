<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$phone = $_POST["phone"];

$query = "UPDATE business SET phone='$phone' WHERE id='$businessId'";
mysqli_query($link, $query);
?>