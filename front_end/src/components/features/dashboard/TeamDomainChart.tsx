import { useMemo } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

interface TeamDomainChartProps {
  data?: Record<string, number>
}

const TeamDomainChart = ({ data }: TeamDomainChartProps) => {
  const chartData = useMemo(() => {
    if (!data) {
      return {
        series: [],
        options: {
          chart: {
            type: 'polarArea' as const,
            height: 350,
            background: 'transparent',
            toolbar: { show: false }
          },
          labels: [],
          noData: {
            text: '데이터를 불러오는 중...'
          }
        }
      }
    }
    const labels = Object.keys(data)
    const series = Object.values(data)

    return {
      series,
      options: {
        chart: {
          type: 'polarArea' as const,
          height: 350,
          background: 'transparent',
          toolbar: {
            show: false
          }
        },
        labels,
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
    }
  }, [data])

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
