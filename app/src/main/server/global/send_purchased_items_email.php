<?php
$emailTo = $_POST["emailTo"];
$emailFrom = $_POST["emailFrom"];
$userName = $_POST["userName"];
$data = $_POST["data"];
$decodedData = json_decode($data);

//create basic message
$message = "";
$message .= "<html><body>";
$message .= "<p>Dear $emailTo,</p>";
$message .= "<p>You received a purchase from $userName via Spoodle!</p>";
$message .= "<p>$userName purchased these items from you:</p>";

//iterate through each item within the decoded json array, and edit email message
for($i = 0; $i < count($decodedData); $i++) {
	$message .= "<p>&#9679;" . "(" . $decodedData[$i][3] . ") " . $decodedData[$i][1] . " for $" . $decodedData[$i][2] . "</p>";
}

//add the ending for the message
$message .= "<p>Expect $userName to contact you via Phone or E-mail about carryout/delivery options.</p>";
$message .= "<p>Please be prepared to ready the items above for $userName.</p>";
$message .= "<p>If you wish, you may contact $userName via his/her email: $emailFrom</p>";
$message .= "<p>Sincerely, The Spoodle Team</p>";
$message .= "</html></body>";

//HTML email headers and mail function
$headers  = 'MIME-Version: 1.0' . "\r\n";
$headers .= 'Content-type: text/html; charset=UTF-8' . "\r\n";
mail($emailTo, "Spoodle - Payment Received", $message, $headers);
?>