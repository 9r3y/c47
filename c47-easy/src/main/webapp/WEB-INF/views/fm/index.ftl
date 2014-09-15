<!DOCTYPE html>
<html>
<head>
    <title>index</title>
    <script type="text/javascript" src="<@spring.url "/"/>resources/js/jquery-1.9.1.js"/>

    <script type="text/javascript">
        function formatUrl(url) {
            return '<@spring.url "/"/>url';
        }
    </script>
    <script type="text/javascript" src="<@spring.url "/"/>resources/js/mvc.js"/>
</head>
<body>
<script type="text/javascript">
    $(function() {
        alert('hello!');
    });
</script>
hello world!
</body>
</html>