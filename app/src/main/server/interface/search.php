<?php
include_once("../db/dbconnection.php");

$search = $_POST["search"];

//get all the businesses with the name that is like the search
$query = "SELECT * FROM business WHERE name LIKE '%" . $search . "%'";

//query the business table for searched businesses
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