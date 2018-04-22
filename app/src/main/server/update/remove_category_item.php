<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$category = $_POST["category"];
$categoryItem = $_POST["categoryItem"];
$price = $_POST["price"];
$quantity = $_POST["quantity"];
$externalities = $_POST["externalities"];

$query = "DELETE FROM category_items WHERE business_id='$businessId' AND category='$category' AND category_item='$categoryItem'";
mysqli_query($link, $query);
?>