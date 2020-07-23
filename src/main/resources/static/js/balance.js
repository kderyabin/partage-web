window.addEventListener('load', () => {
    if(document.querySelector("#chartContainer")) {
        Highcharts.chart('chartContainer', {
            chart: {
                type: 'bar'
            },
            title: {
                text: ''
            },
            xAxis: {
                categories: chartData.categories
            },
            yAxis: {
                min: 0,
                title: {
                    text: notifications.i18n.amount
                }
            },
            legend: {
                reversed: true
            },
            plotOptions: {
                series: {
                    stacking: 'normal'
                }
            },
            series: chartData.series
        });
    }
});
