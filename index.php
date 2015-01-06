<!DOCTYPE html>
<html>
<head>
	<title> Voting System </title>
</head>
<body>
<?php
$servername = "postgres://kisdjufljkdkoc:JPMtcrooqYqaaHc-YSjumnB97v@ec2-54-83-196-7.compute-1.amazonaws.com:5432/d5d7u2801e8s1c ";
$username = "kisdjufljkdkoc";
$password = "JPMtcrooqYqaaHc-YSjumnB97v";


$db_connection = pg_connect("host=ec2-54-83-196-7.compute-1.amazonaws.com dbname=d5d7u2801e8s1c user=kisdjufljkdkoc password=JPMtcrooqYqaaHc-YSjumnB97v "); 
if (!$db_connection) {
    echo "Connection Failed.";
    exit;
}


$course = htmlspecialchars($_GET["course"]);
$string = htmlspecialchars($_GET["string"]);
$assignment = htmlspecialchars($_GET["assignment"]);
$changed = htmlspecialchars($_GET["changed"]);

//echo $course ;
//echo $string ;
//echo $assignment ;

echo $string;
//check to see if string is in generated
$generated_result = pg_query_params($db_connection, 'SELECT cid FROM generated WHERE string=$1;', array($string)) or die ("Database Failed 2");
//$generated_row = pg_fetch_row($result);
echo pg_num_rows($generated_result);

if (pg_num_rows($generated_result) == 1){
	echo 'thank you for your vote' . "\n";
	pg_query_params($db_connection, 'DELETE FROM generated WHERE string=$1;', array($string)) or die ("Database Failed 3");

	$result2 = pg_query_params($db_connection, 'SELECT * FROM assignment WHERE cid=$1 and title=$2;', array($course,$assignment)) or die ("Database Failed 1");

	$row = pg_fetch_row($result2);
	//check to see if its verified
	//if ($row[8] == "false"){
		//update values
		//$votes = pg_query_params($db_connection, 'SELECT votes FROM assignment WHERE id=$1', array(row[0])) or die ("Database Failed 2");
		$votes = pg_query_params($db_connection, 'SELECT votes FROM assignment WHERE title=$1 and cid=$2;', array($assignment,$course)) or die ("Database Failed 2");
		$rowvotes = pg_fetch_row($votes);
		$myvotes = $rowvotes[0]+1;
//		echo $myvotes ."my votes";

		pg_query_params($db_connection,'UPDATE assignment SET votes=$3 WHERE title=$1 and cid=$2;',array($assignment,$course,$myvotes));


		//check to see if it reaches 30% threshold
		$count = pg_query_params($db_connection, 'SELECT COUNT(email) FROM enrolled WHERE cid=$1;', array($course)) or die ("Database Failed");
		$rowcount = pg_fetch_row($count);
		$countcompare = $rowcount[0]*0.5;
		if ($countcompare <= $myvotes){
			if ($changed == ""){
				echo "this assignment has been verified";
				pg_query_params($db_connection,'UPDATE assignment SET verified=$3 WHERE title=$1 and cid=$2;',array($assignment,$course,'true'));
				pg_query_params($db_connection,'DELETE FROM generated WHERE cid=$2 and assignment =(SELECT aid from assignment WHERE title=$1 and cid=$2) ;',array($assignment,$course));
			}
			else{
				echo "this assignment has been updated";
				//loop code here
				//update the table based on loop code
				//$changed = str_replace(",", "', ", $changed);
				$changed = str_replace("/", " ", $changed);
				//$changed = str_replace("=","='",$changed);
				//$changed = $changed . "'";
				//echo $changed;
				//$content = "due_date= 01 01 14,due_time=11:00,weight=20";
				$chars = explode(',', $changed);


				$values = array();

				foreach ($chars as $i) {

					$arr = explode('=', $i);
					array_push($values, $arr[1]);

				}
				//print_r($values)​;

				pg_query_params($db_connection,'UPDATE assignment SET due_date=$3, due_time=$4, weight=$5, votes=0 WHERE title=$1 and cid=$2;',array($assignment,$course,$values[0],$values[1],$values[2]));
				
				pg_query_params($db_connection,'DELETE FROM generated WHERE cid=$2 and assignment =(SELECT aid from assignment WHERE title=$1 and cid=$2) ;',array($assignment,$course));


			}
		
		}

		
	//}

	//$result4 = pg_query_params($db_connection, 'SELECT * FROM assignment WHERE cid=$1 and title=$2;', array($course,$assignment)) or die ("Database Failed 1");

	//$row2 = pg_fetch_row($result4);


//	echo $row2[0] . "  ";
//	echo $row2[1] . "  ";
//	echo $row2[2] . "  ";
//	echo $row2[3] . "  ";
//	echo $row2[4] . "  ";
//	echo $row2[5] . "  ";
//	echo $row2[6] . "  ";
//	echo $row2[7] . "  ";
//	echo $row2[8] . "  ";
//	echo $row2[9] . "\n";


}
else{
	echo 'Not a valid key or this assignment has already been verified';
	
}



pg_close($db_connection);
?> 

</body>
</html>