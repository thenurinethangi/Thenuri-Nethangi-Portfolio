var colorBoxes = $(".color-box"); 

function colorChange() {
    let lastColor;

    for (let i = colorBoxes.length - 1; i >= 0; i--) {
        
        if (i === colorBoxes.length - 1) {
            lastColor = $(colorBoxes[i]).css("background-color"); 
        }

        if (i === 0) {
            $(colorBoxes[i]).css("background-color", lastColor); 
        } 
        else {
            let prevColor = $(colorBoxes[i - 1]).css("background-color"); 
            $(colorBoxes[i]).css("background-color", prevColor); 
        }
    }
}

setInterval(colorChange, 1000);
