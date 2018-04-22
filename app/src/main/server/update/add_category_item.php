<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$category = $_POST["category"];
$categoryItem = $_POST["categoryItem"];
$price = $_POST["price"];
$quantity = $_POST["quantity"];
$externalities = $_POST["externalities"];

$query = "INSERT INTO category_items (business_id, category, category_item, price, quantity, externalities) VALUES('$businessId', '$category','$categoryItem', '$price', '$quantity', '$externalities')";
mysqli_query($link, $query);
?>