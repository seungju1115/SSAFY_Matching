import { useMemo, useState } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'
import { Button } from '@/components/ui/button'
import { mockDevelopers } from '@/data/mockData'

interface PositionDonutChartProps {
  defaultView?: 'primary' | 'secondary'
}

const PositionDonutChart = ({ defaultView = 'primary' }: PositionDonutChartProps) => {
  const [view, setView] = useState<'primary' | 'secondary'>(defaultView)

  const { series, options } = useMemo(() => {
    // 역할 카테고리 정의
    const labels = ['백엔드', '프론트엔드', 'AI', '디자인', 'PM']
    const colors = ['#7986CB', '#64B5F6', '#81C784', '#4DB6AC', '#90CAF9']

    // 주 포지션(Primary): developer.role 기준 집계
    const primaryMap = new Map(labels.map(l => [l, 0])) as Map<string, number>
    mockDevelopers.forEach(dev => {
      if (labels.includes(dev.role)) {
        primaryMap.set(dev.role, (primaryMap.get(dev.role) || 0) + 1)
      }
    })
    const primaryData = labels.map(l => primaryMap.get(l) || 0)

    // 부 포지션(Secondary): positions 중 role과 다른 첫 번째 포지션 집계
    const secondaryMap = new Map(labels.map(l => [l, 0])) as Map<string, number>
    mockDevelopers.forEach(dev => {
      const subs = (dev.positions || []).filter(p => p && p !== dev.role)
      const first = subs.find(p => labels.includes(p))
      if (first) {
        secondaryMap.set(first, (secondaryMap.get(first) || 0) + 1)
      }
    })
    const secondaryData = labels.map(l => secondaryMap.get(l) || 0)

    const opts: ApexOptions = {
      chart: {
        type: 'donut',
        height: 350,
        background: 'transparent',
        toolbar: { show: false }
      },
      labels,
      colors,
      dataLabels: {
        enabled: true,
        formatter: (val: number) => `${Math.round(val)}%`,
        style: {
          fontSize: '12px',
          fontFamily: 'Inter, sans-serif',
          fontWeight: 'bold'
        }
      },
      plotOptions: {
        pie: {
          donut: {
            size: '65%',
            labels: {
              show: true,
              total: {
                show: true,
                label: view === 'primary' ? '주 포지션' : '부 포지션',
                fontSize: '12px',
                formatter: (w: any) => {
                  const total = (w?.globals?.seriesTotals || []).reduce((a: number, b: number) => a + b, 0)
                  return `${total}명`
                }
              },
              value: { show: true, fontSize: '16px', fontWeight: 700 },
              name: { show: true, fontSize: '11px' }
            }
          }
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

    return {
      series: view === 'primary' ? primaryData : secondaryData,
      options: opts
    }
  }, [view])

  return (
    <div className="w-full">
      <div className="flex items-center justify-end gap-2 mb-2">
        <Button
          type="button"
          size="sm"
          variant={view === 'primary' ? 'default' : 'outline'}
          className="h-8 px-3 text-xs"
          onClick={() => setView('primary')}
        >
          주 포지션
        </Button>
        <Button
          type="button"
          size="sm"
          variant={view === 'secondary' ? 'default' : 'outline'}
          className="h-8 px-3 text-xs"
          onClick={() => setView('secondary')}
        >
          부 포지션
        </Button>
      </div>
      {/* key로 강제 리마운트하여 toggle 시 그래프가 확실히 갱신되도록 처리 */}
      <Chart key={view} options={options} series={series} type="donut" height={350} />
    </div>
  )
}

export default PositionDonutChart
