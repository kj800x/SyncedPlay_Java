<?php
$dirname = basename(getcwd());
?>

<html>
  <head>
    <title> SyncedPlay Version: <?=$dirname?> </title>
  </head>
  <body>
    <h2> SyncedPlay Version: <?=$dirname?> </h2> <br />
    <?php
    $files = scandir('.');
    natsort($files);
    foreach($files as $file) {
      if (!($file == "." || $file == ".." || $file == "index.php" )) 
      {
        ?>
          <h3> <a href="./<?=$file?>"> <?=$file?> </a> </h3>
        <?php
      }
    }
    ?>
  </body>
</html>