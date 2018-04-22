<?php
include_once("../db/dbconnection.php");

$businessId = $_POST["businessId"];

$myDate = date("Y-m-d", strtotime( date( "Y-m-d", strtotime( date("Y-m-d") ) ) . "+1 month" ) );

$query = "UPDATE business SET promotion_expiration='$myDate' WHERE id='$businessId'";
mysqli_query($link, $query);
?>