import { useState } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

const TeamDomainChart = () => {
  const [chartData] = useState({
    series: [44, 55, 13, 43, 22],
    options: {
      chart: {
        type: 'polarArea' as const,
        height: 350,
        background: 'transparent',
        toolbar: {
          show: false
        }
      },
      labels: ['웹 개발', '모바일', 'AI/ML', '게임', 'IoT'],
      fill: {
        opacity: 0.8
      },
      colors: ['#FF6B8A', '#FF8A65', '#FFB74D', '#FFAB91', '#F48FB1', '#FFCC80'],
      stroke: {
        width: 1,
        colors: ['#fff']
      },
      yaxis: {
        show: false
      },
      legend: {
        position: 'bottom' as const,
        horizontalAlign: 'center' as const,
        fontSize: '12px',
        fontFamily: 'Inter, sans-serif',
        markers: {
          width: 8,
          height: 8,
          radius: 4
        }
      },
      plotOptions: {
        polarArea: {
          rings: {
            strokeWidth: 1,
            strokeColor: '#e5e7eb'
          },
          spokes: {
            strokeWidth: 1,
            connectorColors: '#e5e7eb'
          }
        }
      },
      responsive: [{
        breakpoint: 480,
        options: {
          chart: {
            height: 300
          },
          legend: {
            fontSize: '10px'
          }
        }
      }]
    } as ApexOptions
  })

  return (
    <div className="w-full">
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="polarArea"
        height={350}
      />
    </div>
  )
}

export default TeamDomainChart
