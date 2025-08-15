import { useMemo } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

interface MajorMinorPieChartProps {
  majorCount?: number
  nonMajorCount?: number
}

const MajorMinorPieChart = ({ majorCount, nonMajorCount }: MajorMinorPieChartProps) => {
  const { series, options } = useMemo(() => {
    // API 데이터가 있으면 사용, 없으면 기본값 사용
    const major = majorCount ?? 0
    const nonMajor = nonMajorCount ?? 0
    const data = [major, nonMajor]

    const opts: ApexOptions = {
      chart: {
        type: 'pie',
        height: 350,
        background: 'transparent',
        toolbar: { show: false }
      },
      labels: ['전공자', '비전공자'],
      colors: ['#475569', '#10B981'], // slate-600, emerald-500
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
  }, [majorCount, nonMajorCount])

  // 로딩 상태 표시
  if (majorCount === undefined || nonMajorCount === undefined) {
    return (
      <div className="w-full h-[350px] flex items-center justify-center">
        <div className="text-gray-500">데이터를 불러오는 중...</div>
      </div>
    )
  }

  return (
    <div className="w-full">
      <Chart 
        key={`major-minor-${majorCount}-${nonMajorCount}`}
        options={options} 
        series={series} 
        type="pie" 
        height={350} 
      />
    </div>
  )
}

export default MajorMinorPieChart
