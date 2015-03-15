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
  <pre style="border: 1px solid black; margin-left: 10px"><?php
      $changelog = file_get_contents("./changelog.txt");
      echo htmlspecialchars($changelog);
    ?></pre>
  </body>
</html>