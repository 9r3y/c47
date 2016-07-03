<!DOCTYPE html>
<html>
<head>
    <title>index</title>
    <script src="${rc.getContextPath()}/static/js/jquery-1.9.1.js"></script>
    <script src="${rc.getContextPath()}/static/js/mvc.js"></script>
</head>
<body>
hello world!
<script>
    function formatUrl(url) {
        return '${rc.getContextPath()}/url';
    }
    $(function() {
        alert('hello!');
    });
</script>
</body>
</html>