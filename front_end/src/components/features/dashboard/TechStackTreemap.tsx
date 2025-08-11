import { useState } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

const TechStackTreemap = () => {
  const [chartData] = useState({
    series: [
      {
        data: [
          { x: 'React', y: 218 },
          { x: 'Vue.js', y: 149 },
          { x: 'Angular', y: 184 },
          { x: 'Node.js', y: 155 },
          { x: 'Spring Boot', y: 112 },
          { x: 'Django', y: 108 },
          { x: 'FastAPI', y: 89 },
          { x: 'Express.js', y: 95 },
          { x: 'Python', y: 165 },
          { x: 'JavaScript', y: 201 },
          { x: 'TypeScript', y: 178 },
          { x: 'Java', y: 142 },
          { x: 'C++', y: 98 },
          { x: 'Go', y: 67 },
          { x: 'Rust', y: 45 },
          { x: 'MySQL', y: 134 },
          { x: 'PostgreSQL', y: 98 },
          { x: 'MongoDB', y: 87 },
          { x: 'Redis', y: 76 },
          { x: 'Docker', y: 156 },
          { x: 'Kubernetes', y: 89 },
          { x: 'AWS', y: 123 },
          { x: 'GCP', y: 78 },
          { x: 'Azure', y: 65 }
        ]
      }
    ],
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
  })

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
