let menu = document.getElementById("menu");
console.log(menu);

let pages = ['Home','About Me','Skill','Project','Contact'];
let links = ['index.html','about-page.html','skill-page.html','project-page.html','contact-page.html'];

menu.addEventListener('click',loadNavigationBar);

function loadNavigationBar(){

    let bar = document.createElement('div');
    bar.style.width = '0%';
    bar.style.height = '0vh';
    bar.style.backgroundColor = 'rgba(20, 17, 26)';
    bar.style.zIndex = '100000';
    bar.style.position = 'absolute';
    bar.style.borderBottomRightRadius = '1000%';
    bar.style.display = 'flex';
    bar.style.flexDirection = 'column';
    bar.style.justifyContent = 'center';
    bar.style.alignItems = 'center';
    bar.style.gap = '3.5em';


    let close = document.createElement('img');
    close.src = 'assets/image/close-button.png';
    close.style.width = '32px';
    close.style.opacity = '0.8';
    close.style.position = 'absolute';
    close.style.top = '0';
    close.style.right = '0';
    close.style.margin = '2.5em 2em 0em 0em';

    close.addEventListener('click',hey);

    function hey(){

        console.log("heyy");
    }

    bar.append(close);

    for (let i = 0; i < 5; i++) {
        let a = document.createElement("a");
        a.innerHTML = pages[i];
        a.style.width = '100%';
        a.style.textDecoration = 'none';
        a.style.fontSize = '1.15rem';
        a.style.textAlign = 'center';
        a.style.color = '#FFFFFF';
        a.href = links[i];

        bar.append(a);
    }
    
    
    
    
    
    

    bar.animate(
        [
            { width: "0%", height: '0vh', borderBottomRightRadius: '1000%',  offset: 0 },
            { width: "100%", height: '100vh', borderBottomRightRadius: '0%', offset: 1 },
        ],
        {
            duration: 800,
            iterations: 1,
            easing: "linear",
            fill: 'forwards'
        }
    );

    let body = document.getElementsByTagName('body');
    body[0].prepend(bar);

}