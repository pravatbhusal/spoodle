<?php
include_once("../db/dbconnection.php");

//get all the businesses whose promotion_expiration has not yet expired
$query = "SELECT * FROM business WHERE promotion_expiration > NOW() - INTERVAL 1 DAY";

//query the business table for non-expired promoted businesses
$businesses = "";
if($result = mysqli_query($link, $query)) {
	while($row = mysqli_fetch_array($result)) {	
		if(mysqli_num_rows($result)) {
			$id = $row["id"];
			$title = $row["name"];
			$time = $row["time"];
			$image = $row["image"];
			$businesses .= '{"id": "'.$id.'", "title": "'.$title.'", "time": "'.$time.'", "image": "'.$image.'"},';
		}
	}
} else {
	exit('{"result": "failure"}');
}

//remove the trailing comma for $businesses
$businesses = rtrim($businesses, ',');

//output the json data
$jsonData = '{
	"result": "success", 
	"businesses": ['.$businesses.']
	}';
exit($jsonData);
?>