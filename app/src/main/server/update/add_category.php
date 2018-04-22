<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$category = $_POST["category"];

$query = "INSERT INTO categories (business_id, category) VALUES('$businessId', '$category')";
mysqli_query($link, $query);
?>