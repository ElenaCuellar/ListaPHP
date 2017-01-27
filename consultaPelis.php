<?php
    $server = "localhost";
    $user = "root";
    $pass = "clave";
    $bd = "BDElena";

    //Creamos la conexión
    $conexion = mysqli_connect($server, $user, $pass,$bd)
    or die("Ha sucedido un error inexperado en la conexion de la base de datos");

    //Generamos la consulta
    $sql = $_REQUEST['ins_sql'];

    mysqli_set_charset($conexion, "utf8"); //formato de datos utf8
    if(!$result = mysqli_query($conexion, $sql)) die();
    $arraydatos = array(); //creamos un array
    while($row = mysqli_fetch_array($result))
    {
        foreach($row as $key => $value)
        {
            $arr[$key] = $value;
        }
        $arraydatos[] = $arr;

    }

    //desconectamos la base de datos
    $close = mysqli_close($conexion)
    or die("Ha sucedido un error inexperado en la desconexion de la base de datos");

    //Creamos el JSON
    $json_string = json_encode($arraydatos);
    echo $json_string;
?>
