<?php
include_once("../db/dbconnection.php");
date_default_timezone_set('America/Los_Angeles');

$businessId = $_POST["businessId"];
$data = $_POST["data"];
$decodedData = json_decode($data);

//update the quantity of items available based on the purchased list here
$totalRevenue = 0;
for($i = 0; $i < count($decodedData); $i++) {
	$category = $decodedData[$i][0];
	$quantity = $decodedData[$i][3];
	$categoryItem = $decodedData[$i][1];
	$price = $decodedData[$i][2];
	
	//subtract the quantity available
	$query = "UPDATE category_items SET quantity=quantity - '$quantity' WHERE business_id='$businessId' AND category='$category' AND category_item='$categoryItem'";
	mysqli_query($link, $query);
	$totalRevenue += $price * $quantity;
}

//get all the businesses information based on the id
$weekly_total_revenue = "";
$last_updated = "";
$query = "SELECT * FROM business WHERE id='$businessId'";
if($result = mysqli_query($link, $query)) { //if query was a success!
	if(mysqli_num_rows($result)) {
		$row = mysqli_fetch_array($result);
		$weekly_total_revenue = $row["weekly_total_revenue"];
		$last_updated = $row["last_updated"];
	}
}

//update the business's weekly total revenue
$now = strtotime(date("Y-m-d h:i:sa"));
$last_updated = strtotime($last_updated . " " . date("h:i:sa"));
$dateDifference = round(($now - $last_updated) / (60 * 60 * 24));
$dayWeekDifference = $dateDifference % 7;

$weekly_total_revenue_read = explode("," , $weekly_total_revenue);
$weekly_total_revenue_update = explode("," , $weekly_total_revenue);

//if the date difference is greater or equal to the weekly total length, then we reset the entire update array to 0
if($dateDifference >= count($weekly_total_revenue_read)) {
	$weekly_total_revenue_update = array_fill_keys(array_keys($weekly_total_revenue_update), 0);
} else {
	//will iterate based on the length of the weekly_total_revenue (7)
	for($i = 0; $i < count($weekly_total_revenue_read); $i++) {
		//for the days missed from the last revenue update, set them to zero
		if($i < $dayWeekDifference) {
			$weekly_total_revenue_update[$i] = 0;
		}
		//make sure that the updated index doesn't surpass the number of indexes possible
		if($i + $dayWeekDifference < count($weekly_total_revenue_read)) {
			//shift the revenues based on the updated dates
			$weekly_total_revenue_update[$i + $dayWeekDifference] = $weekly_total_revenue_read[$i];
		}
	}
}

//update today's revenue values (index of 0)
$weekly_total_revenue_update[0] += $totalRevenue;
$weekly_total_revenue = implode("," , $weekly_total_revenue_update);

//update weekly_total_revenue sql column to $weekly_total_revenue here
$query = "UPDATE business SET weekly_total_revenue='$weekly_total_revenue' WHERE id='$businessId'";
mysqli_query($link, $query);

//update last_updated sql column to today's date here
$todaysDate = date("Y-m-d");  
$query = "UPDATE business SET last_updated='$todaysDate' WHERE id='$businessId'";
mysqli_query($link, $query);
?>