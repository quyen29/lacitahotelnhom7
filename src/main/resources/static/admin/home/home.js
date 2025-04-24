<script th:inline="javascript">
    const labels = [[${chartLabels}]];
    const data = [[${chartData}]];

    const ctx = document.getElementById('revenueChart').getContext('2d');
    const revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu',
                data: data,
                borderColor: '#2ecc71',
                backgroundColor: 'rgba(46, 204, 113, 0.2)',
                borderWidth: 2,
                fill: true,
                tension: 0,
                pointBackgroundColor: '#2ecc71',
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        title: function(context) {
                            return context[0].label;
                        },
                        label: function(context) {
                            return 'Doanh thu: ' + context.formattedValue.toLocaleString('vi-VN') + ' VND';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 10000000,
                        callback: function(value) {
                            return value.toLocaleString('vi-VN');
                        }
                    }
                }
            }
        }
    });
</script>
