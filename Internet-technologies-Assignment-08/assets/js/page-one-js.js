var box = $(".man-box");

function rotate() {

    var insideText = [];

    for (let i = 0; i < box.length; i++) {
        let text = $(box[i]).children().eq(1).text();
        insideText.push(text);
    }

    for (let i = 0; i < box.length; i++) {
        if (i === 0) {
            $(box[i]).children().eq(1).html(insideText[insideText.length - 1]);
        }
        else {
            $(box[i]).children().eq(1).html(insideText[i - 1]);
        }
    }
}

setInterval(rotate, 1000);
