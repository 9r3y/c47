<!DOCTYPE html>
<html>
<head>
    <title>index</title>
    <script type="text/javascript" src="<@spring.url "/"/>resources/js/jquery-1.9.1.js"/></script>

    <script type="text/javascript">
        function formatUrl(url) {
            return '<@spring.url "/"/>' + url;
        }
    </script>

    <script type="text/javascript" src="<@spring.url "/"/>resources/js/cu.js"/></script>
    <script type="text/javascript">
        $(function() {
            var images = [{name: 'starcraft_32', url: formatUrl('resources/image/starcraft.ico')}]

            canvasUtils.loadImages(images, function() {
                var $div1 = $('#div1');
                draw($div1, canvasUtils);
            })

        });
        function draw($parent, cu) {
            var panel = new cu.Panel();
            //panel.setWidth($parent.width());
            //panel.setHeight($parent.height());

            var node = new cu.Node();
            node.setLocation(200, 200);
            node.setStyle('outline.width', 4);
            node.setStyle('padding', 4);
            node.setStyle('outline.color', '#00FF00');
            node.setImage('starcraft_32');
            node.setName('fefefeafeg');
            node.getEventManager().on('click', function() {
                alert('fe');
                /*var cx = this.viewRect.cx;
                var cy = this.viewRect.cy;
                this.setStyle('outline.width', 5);
                this.setCenterLocation(cx, cy);*/
            });
            node.getEventManager().on('dblclick', function() {
                alert('fefe');
            });
            panel.addElement(node);

            $parent.append(panel.getView());
        }
    </script>
</head>
<body>

<div id="div1"></div>

</body>
</html>

