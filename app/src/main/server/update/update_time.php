<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$time = $_POST["time"];

$query = "UPDATE business SET time='$time' WHERE id='$businessId'";
mysqli_query($link, $query);
?>