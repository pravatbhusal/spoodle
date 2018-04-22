<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$category = $_POST["category"];

//get all the category items within the business_id
$query = "SELECT * FROM category_items WHERE business_id='$businessId' AND category='$category'";

//query the category items table for category items under the business_id
$categoryItems = "";
if($result = mysqli_query($link, $query)) {
	while($row = mysqli_fetch_array($result)) {	
		if(mysqli_num_rows($result)) {
			$categoryItem = $row["category_item"];
			$price = $row["price"];
			$quantity = $row["quantity"];
			$externalities = $row["externalities"];
			$categoryItems .= '{"category": "'.$category.'", "categoryItem": "'.$categoryItem.'",
			"price": "'.$price.'", "quantity": "'.$quantity.'", "externalities": "'.$externalities.'"},';
		}
	}
} else {
	exit('{"result": "failure"}');
}

//remove the trailing comma for $categoryItems
$categoryItems = rtrim($categoryItems, ',');

//output the json data
$jsonData = '{
	"result": "success", 
	"categoryItems": ['.$categoryItems.']
	}';
exit($jsonData);
?>