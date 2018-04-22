<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];

//get all the businesses information based on the id
$query = "SELECT * FROM business WHERE id='$businessId'";
if($result = mysqli_query($link, $query)) { //if query was a success!
	if(mysqli_num_rows($result)) {
		$row = mysqli_fetch_array($result);
		$businessName = $row["name"];
		$businessImage = $row["image"];
		$businessEmail = $row["email"];
		$businessAddress = $row["address"];
		$businessPhone = $row["phone"];
		$weeklyTotalRevenue = $row["weekly_total_revenue"];
		$time = $row["time"];
		
		//exit the data
		exit('{
		"result": "success",
		"businessName": "'.$businessName.'",
		"businessImage": "'.$businessImage.'",
		"businessEmail": "'.$businessEmail.'",
		"businessAddress": "'.$businessAddress.'",
		"businessPhone": "'.$businessPhone.'",
		"weeklyTotalRevenue": "'.$weeklyTotalRevenue.'",
		"time": "'.$time.'"
		}');
	}
}

exit('{"result": "failure"}');
?>