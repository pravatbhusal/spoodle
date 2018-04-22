<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$category = $_POST["category"];
$oldCategoryItem = $_POST["oldCategoryItem"];
$newCategoryItem = $_POST["newCategoryItem"];
$price = $_POST["price"];
$quantity = $_POST["quantity"];
$externalities = $_POST["externalities"];

$query = "UPDATE category_items SET category_item='$newCategoryItem', price='$price', quantity='$quantity', externalities='$externalities' WHERE business_id='$businessId' AND category='$category' AND category_item='$oldCategoryItem' ";
mysqli_query($link, $query);
?>