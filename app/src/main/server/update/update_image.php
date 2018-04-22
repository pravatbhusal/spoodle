<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];
$businessImage = $_POST["businessImage"];
$uploadImagePath = "../media/business/" . "business_" . $businessId;

//if media folder doesn't exist, then create one
if (!file_exists("..media/")) {
    mkdir("..media/", 0777, true);
}

//if media/business folder doesn't exist, then create one
if (!file_exists("..media/business/")) {
    mkdir("..media/business/", 0777, true);
}

//if business folder doesn't exist, then create one
if (!file_exists($uploadImagePath)) {
    mkdir($uploadImagePath, 0777, true);
}

//input the image's path into the database here
$imageURL = "/media/business/" . "business_" . $businessId . "/image.jpg";
$query = "UPDATE business SET image='$imageURL' WHERE id='$businessId'";

//once the server queries the database successfully, then add the image into the correct directory
if(mysqli_query($link, $query)) {
	file_put_contents($uploadImagePath . "/image.jpg", base64_decode($businessImage));
}
?>