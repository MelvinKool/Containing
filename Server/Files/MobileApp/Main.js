window.onkeydown = checkKey;

var chart;
var xhttp = new XMLHttpRequest();

function main(){
    Chart.defaults.global = {
        animation: true,
        animationSteps: 60,
        animationEasing: "easeOutQuart",
        showScale: true,
        scaleOverride: false,
        scaleSteps: null,
        scaleStepWidth: null,
        scaleStartValue: null,
        scaleLineColor: "rgba(0,0,0,0.5)",
        scaleLineWidth: 1,
        scaleShowLabels: true,
        scaleLabel: "<%=value%>",
        scaleIntegersOnly: true,
        scaleBeginAtZero: false,
        scaleFontFamily: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
        scaleFontSize: 12,
        scaleFontStyle: "normal",
        scaleFontColor: "#000000",
        responsive: true,
        maintainAspectRatio: false,
        showTooltips: true,
        customTooltips: false,
        tooltipEvents: ["mousemove", "touchstart", "touchmove"],
        tooltipFillColor: "rgba(0,0,0,0.8)",
        tooltipFontFamily: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
        tooltipFontSize: 14,
        tooltipFontStyle: "normal",
        tooltipFontColor: "#fff",
        tooltipTitleFontFamily: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
        tooltipTitleFontSize: 14,
        tooltipTitleFontStyle: "bold",
        tooltipTitleFontColor: "#fff",
        tooltipYPadding: 6,
        tooltipXPadding: 6,
        tooltipCaretSize: 8,
        tooltipCornerRadius: 6,
        tooltipXOffset: 10,
        tooltipTemplate: "<%if (label){%><%=label%>: <%}%><%= value %>",
        multiTooltipTemplate: "<%= value %>",
        onAnimationProgress: function(){},
        onAnimationComplete: function(){}
    };
    var data = {
        labels: ["Zeeschip", "Binnenschip", "AGV", "Trein", "Vrachtauto", "Opslag", "Diversen"],
        datasets: [
            {
                fillColor: "rgba(58, 147, 241, 0.8)",
                strokeColor: "rgba(11, 129, 251, 0.8)",
                data: [0,0,0,0,0,0,0]
            }
        ]
    };
    var options = {
        scaleBeginAtZero : true,
        scaleShowGridLines : true,
        scaleGridLineColor : "rgba(0,0,0,0.2)",
        scaleGridLineWidth : 1,
        scaleShowHorizontalLines: true,
        scaleShowVerticalLines: true,
        barShowStroke : true,
        barStrokeWidth : 2,
        barValueSpacing : 5,
        barDatasetSpacing : 1,
        legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].fillColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
    };
    var ctx = document.getElementById("chartCanvas").getContext("2d");
    chart = new Chart(ctx).Bar(data, options);
    setInterval(update, 1000);
}

function update(){
    xhttp.open("GET", "DATA.json", true);
    xhttp.send();
}

xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
        var response = xhttp.responseText;
        response = response.split(",");
        for(var i = 0; i < 7; i++){
            chart.datasets[0].bars[i].value = parseInt(response[i]);
        }
        chart.update();
    }
};

//------------------------------------------------------------------------------
var i = 0;
function checkKey(ev){
    switch(ev.keyCode)
    {
        case 49:{
            i += 5;
            chart.datasets[0].bars[0].value = i;
            chart.update();
            break;
        }
        case 50:{
            update();
            break;
        }
    }
}
