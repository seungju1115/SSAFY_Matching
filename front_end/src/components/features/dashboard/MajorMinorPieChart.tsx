import { useMemo } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'
import { mockDevelopers } from '@/data/mockData'

const MajorMinorPieChart = () => {
  const { series, options } = useMemo(() => {
    const major = mockDevelopers.filter(d => d.isMajor).length
    const nonMajor = mockDevelopers.filter(d => !d.isMajor).length

    const data = [major, nonMajor]

    const opts: ApexOptions = {
      chart: {
        type: 'pie',
        height: 350,
        background: 'transparent',
        toolbar: { show: false }
      },
      labels: ['전공자', '비전공자'],
      colors: ['#475569', '#10B981'], // slate-600, emerald-500_
      dataLabels: {
        enabled: true,
        formatter: (val: number) => `${Math.round(val)}%`,
        style: {
          fontSize: '12px',
          fontFamily: 'Inter, sans-serif',
          fontWeight: 'bold'
        }
      },
      legend: {
        position: 'bottom',
        horizontalAlign: 'center',
        fontSize: '12px',
        fontFamily: 'Inter, sans-serif',
        markers: { size: 8 }
      },
      tooltip: {
        y: { formatter: (val: number) => `${val}명` }
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: { height: 300 },
            legend: { fontSize: '10px' },
            dataLabels: { style: { fontSize: '10px' } }
          }
        }
      ]
    }

    return { series: data, options: opts }
  }, [])

  return (
    <div className="w-full">
      <Chart options={options} series={series} type="pie" height={350} />
    </div>
  )
}

export default MajorMinorPieChart
