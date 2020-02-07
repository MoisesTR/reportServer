
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Page Not Found</title>
    <style type="text/css">
        img {
            width: 100%;
            height: auto;
        }

        body {
            /* Location of the image */
            background-image: url("${pageContext.request.contextPath}/images/404.jpg");

            /* Background image is centered vertically and horizontally at all times */
            background-position: center center;

            /* Background image doesn't tile */
            background-repeat: no-repeat;

            /* Background image is fixed in the viewport so that it doesn't move when
               the content's height is greater than the image's height */
            background-attachment: fixed;

            /* This is what makes the background image rescale based
               on the container's size */
            background-size: cover;

            /* Set a background color that will be displayed
               while the background image is loading */
            background-color: #464646;
        }

    </style>
</head>
<body>
</body>
</html>