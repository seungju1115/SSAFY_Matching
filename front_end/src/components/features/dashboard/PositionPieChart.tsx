import { useState, useEffect } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

interface PositionPieChartProps {
  type: 'primary' | 'secondary'
}

const PositionPieChart = ({ type }: PositionPieChartProps) => {
  const [chartData, setChartData] = useState({
    series: [44, 55, 13, 43, 22],
    options: {
      chart: {
        type: 'pie' as const,
        height: 350,
        background: 'transparent',
        toolbar: {
          show: false
        }
      },
      labels: ['백엔드', '프론트엔드', 'AI', '디자인', 'PM'],
      colors: ['#7986CB', '#64B5F6', '#81C784', '#4DB6AC', '#90CAF9'],
      dataLabels: {
        enabled: true,
        formatter: function (val: number) {
          return Math.round(val) + "%"
        },
        style: {
          fontSize: '12px',
          fontFamily: 'Inter, sans-serif',
          fontWeight: 'bold'
        }
      },
      plotOptions: {
        pie: {
          donut: {
            size: '0%'
          }
        }
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
      tooltip: {
        y: {
          formatter: function (val: number) {
            return val + "명"
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
          },
          dataLabels: {
            style: {
              fontSize: '10px'
            }
          }
        }
      }]
    } as ApexOptions
  })

  useEffect(() => {
    // 주 포지션과 부 포지션에 따라 다른 데이터 설정
    const primaryData = [145, 120, 85, 65, 45] // 주 포지션 데이터
    const secondaryData = [95, 110, 75, 55, 35] // 부 포지션 데이터 (조금 다른 분포)

    setChartData(prev => ({
      ...prev,
      series: type === 'primary' ? primaryData : secondaryData
    }))
  }, [type])

  return (
    <div className="w-full">
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="pie"
        height={350}
      />
    </div>
  )
}

export default PositionPieChart
