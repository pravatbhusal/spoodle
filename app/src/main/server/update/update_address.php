<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$address = $_POST["address"];

$query = "UPDATE business SET address='$address' WHERE id='$businessId'";
mysqli_query($link, $query);
?>