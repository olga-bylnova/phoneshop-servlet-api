$("a[id*='price']").hover(
    function () {
        console.log("hover");
        $(this).find('#priceHistory').css('display', 'block')
    },
    function () {
        $(this).find('#priceHistory').css('display', 'none')
    }
);