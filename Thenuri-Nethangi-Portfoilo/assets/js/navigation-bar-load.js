let menu = document.getElementById("menu");
console.log(menu);

let pages = ['Home', 'About Me', 'Skill', 'Project', 'Contact'];
let links = ['index.html', 'about-page.html', 'skill-page.html', 'project-page.html', 'contact-page.html'];

menu.addEventListener('click', loadNavigationBar);

function loadNavigationBar() {

    let anchors = [];

    let bar = document.createElement('div');
    bar.style.width = '100vw';
    bar.style.height = '0vh';
    bar.style.backgroundColor = 'rgba(20, 17, 26)';
    bar.style.zIndex = '100000';
    bar.style.position = 'fixed';
    bar.style.right = '0';
    bar.style.top = '0';
    bar.style.borderBottomLeftRadius = '1000%';
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

    close.addEventListener('click', closeNavBar);

    function closeNavBar() {
        bar.animate(
            [
                { width: "100vw", height: '100vh', borderBottomLeftRadius: '0%', offset: 0 },
                { width: "0vw", height: '0vh', borderBottomLeftRadius: '1000%', offset: 1 },
            ],
            {
                duration: 800,
                iterations: 1,
                easing: "ease-in-out",
                fill: 'forwards'
            }
        );

        close.animate(
            [
                { opacity: '1', offset: 0 },
                { opacity: '0', offset: 1 }
            ],
            {
                duration: 800,
                iterations: 1,
                easing: "ease-in-out",
                fill: 'both'
            }
        );

        anchors.forEach(anchor => {
            anchor.animate(
                [
                    { opacity: '1', offset: 0 },
                    { opacity: '0', offset: 1 }
                ],
                {
                    duration: 500,
                    easing: "ease-in-out",
                    fill: 'both'
                }
            );
        });

        setTimeout(() => bar.remove(), 800);
    }

    bar.append(close);

    for (let i = 0; i < pages.length; i++) {
        let a = document.createElement("a");
        a.innerHTML = pages[i];
        a.style.textDecoration = 'none';
        a.style.fontSize = '1.15rem';
        a.style.textAlign = 'center';
        a.style.color = '#FFFFFF';
        a.style.fontWeight = '300';
        a.href = links[i];
        anchors.push(a);

        a.addEventListener('mouseenter', () => {
            a.style.background = 'linear-gradient(to right, #FFFFFF, #9747FF)';
            a.style.webkitBackgroundClip = 'text';
            a.style.webkitTextFillColor = 'transparent';
        });

        a.addEventListener('mouseleave', () => {
            a.style.background = 'rgba(20, 17, 26)';
            a.style.webkitTextFillColor = '#FFFFFF';
            a.style.color = '#FFFFFF';
        });

        a.addEventListener('click', closeNavBarTwo);

        function closeNavBarTwo() {
            bar.animate(
                [
                    { width: "100vw", height: '100vh', borderBottomLeftRadius: '0%', offset: 0 },
                    { width: "0vw", height: '0vh', borderBottomLeftRadius: '1000%', offset: 1 },
                ],
                {
                    duration: 100,
                    iterations: 1,
                    easing: "ease-in-out",
                    fill: 'forwards'
                }
            );

            anchors.forEach(anchor => {
                anchor.animate(
                    [
                        { opacity: '1', offset: 0 },
                        { opacity: '0', offset: 1 }
                    ],
                    {
                        duration: 100,
                        easing: "ease-in-out",
                        fill: 'both'
                    }
                );
            });

            setTimeout(() => bar.remove(), 100);
        }

        bar.append(a);
    }


    bar.animate(
        [
            { width: "0vw", height: '0vh', borderBottomLeftRadius: '1000%', offset: 0 },
            { width: "100vw", height: '100vh', borderBottomLeftRadius: '0%', offset: 1 },
        ],
        {
            duration: 800,
            iterations: 1,
            easing: "ease-in-out",
            fill: 'forwards'
        }
    );

    anchors.forEach(anchor => {
        anchor.animate(
            [
                { opacity: '0', offset: 0 },
                { opacity: '1', offset: 1 }
            ],
            {
                duration: 800,
                easing: "ease-in-out",
                fill: 'both'
            }
        );
    });

    document.body.prepend(bar);
}
