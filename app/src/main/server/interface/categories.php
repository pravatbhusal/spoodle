<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];

//get all the categories within the business_id
$query = "SELECT * FROM categories WHERE business_id='$businessId'";

//query the categories table for categories under the business_id
$categories = "";
if($result = mysqli_query($link, $query)) {
	while($row = mysqli_fetch_array($result)) {	
		if(mysqli_num_rows($result)) {
			$category = $row["category"];
			$categories .= '{"category": "'.$category.'"},';
		}
	}
} else {
	exit('{"result": "failure"}');
}

//remove the trailing comma for $categories
$categories = rtrim($categories, ',');

//output the json data
$jsonData = '{
	"result": "success", 
	"categories": ['.$categories.']
	}';
exit($jsonData);
?>