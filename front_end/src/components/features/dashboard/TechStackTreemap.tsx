import { useMemo } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

interface TechStackTreemapProps {
  data?: Record<string, number>
}

const TechStackTreemap = ({ data }: TechStackTreemapProps) => {
  const chartData = useMemo(() => {
    if (!data) {
      return {
        series: [{ data: [] }],
        options: {
          chart: {
            type: 'treemap' as const,
            height: 350,
            background: 'transparent',
            toolbar: { show: false }
          },
          noData: {
            text: '데이터를 불러오는 중...'
          }
        }
      }
    }
    const techStackData = Object.entries(data).map(([tech, count]) => ({ x: tech, y: count }))

    return {
      series: [{ data: techStackData }],
    options: {
      chart: {
        type: 'treemap' as const,
        height: 350,
        background: 'transparent',
        toolbar: {
          show: false
        }
      },
      colors: [
        '#7986CB', '#64B5F6', '#81C784', '#4DB6AC', '#90CAF9', '#9FA8DA', '#80CBC4', '#A5D6A7'
      ],
      plotOptions: {
        treemap: {
          enableShades: true,
          shadeIntensity: 0.5,
          reverseNegativeShade: true,
          colorScale: {
            ranges: [
              { from: 0, to: 50, color: '#CD363A' },
              { from: 51, to: 100, color: '#F59E0B' },
              { from: 101, to: 150, color: '#10B981' },
              { from: 151, to: 250, color: '#3B82F6' }
            ]
          }
        }
      },
      dataLabels: {
        enabled: true,
        style: {
          fontSize: '12px',
          fontFamily: 'Inter, sans-serif',
          fontWeight: 'bold',
          colors: ['#fff']
        },
        formatter: function(text: string, op: any) {
          return [text, op.value + '명']
        }
      },
      tooltip: {
        y: {
          formatter: function (val: number) {
            return val + "명이 사용 중"
          }
        }
      },
      responsive: [{
        breakpoint: 480,
        options: {
          chart: {
            height: 300
          },
          dataLabels: {
            style: {
              fontSize: '10px'
            }
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
        type="treemap"
        height={350}
      />
    </div>
  )
}

export default TechStackTreemap
