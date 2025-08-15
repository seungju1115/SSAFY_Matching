import { useState, useEffect } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

interface TeamPositionChartProps {
  selectedDomain: string
  data?: Record<string, number[]>
}

const TeamPositionChart = ({ selectedDomain, data }: TeamPositionChartProps) => {
  const [chartData, setChartData] = useState({
    series: [{
      name: '인원 수',
      data: [65, 120, 35, 25, 15] // 기본값: 웹 개발 도메인 데이터
    }],
    options: {
      chart: {
        type: 'bar' as const,
        height: 350,
        background: 'transparent',
        toolbar: {
          show: false
        },
        distributed: true
      },
      colors: ['#FF6B8A', '#FF8A65', '#FFB74D', '#FFAB91', '#F48FB1'],
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '60%',
          endingShape: 'rounded',
          distributed: true
        }
      },
      dataLabels: {
        enabled: true,
        style: {
          fontSize: '12px',
          fontFamily: 'Inter, sans-serif',
          fontWeight: 'bold',
          colors: ['#fff']
        }
      },
      stroke: {
        show: true,
        width: 2,
        colors: ['transparent']
      },
      xaxis: {
        categories: ['백엔드', '프론트엔드', 'AI', '디자인', 'PM'],
        labels: {
          style: {
            fontSize: '12px',
            fontFamily: 'Inter, sans-serif'
          }
        }
      },
      yaxis: {
        title: {
          text: '인원 수',
          style: {
            fontSize: '12px',
            fontFamily: 'Inter, sans-serif'
          }
        },
        labels: {
          style: {
            fontSize: '12px',
            fontFamily: 'Inter, sans-serif'
          }
        }
      },
      fill: {
        opacity: 1
      },
      tooltip: {
        y: {
          formatter: function (val: number) {
            return val + "명"
          }
        }
      },
      legend: {
        show: false // 단일 시리즈이므로 범례 숨김
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

  // 선택된 도메인에 따라 데이터 업데이트
  useEffect(() => {
    const getDomainData = (domain: string) => {
      if (data) {
        // 실제 API 데이터가 있을 때
        if (domain === 'all') {
          // 전체 도메인 합계 계산
          const allDomains = Object.values(data)
          const totalData = [0, 0, 0, 0, 0] // [백엔드, 프론트엔드, AI, 디자인, PM]
          allDomains.forEach(domainArray => {
            domainArray.forEach((count, index) => {
              totalData[index] += count
            })
          })
          return totalData
        } else {
          // 특정 도메인 선택 시 해당 도메인의 데이터 반환
          return data[domain] || [0, 0, 0, 0, 0]
        }
      } else {
        // API 데이터가 없으면 빈 배열 반환
        return [0, 0, 0, 0, 0]
      }
    }

    const positionData = getDomainData(selectedDomain)
    
    setChartData(prev => ({
      ...prev,
      series: [{
        name: '인원 수',
        data: positionData
      }]
    }))
  }, [selectedDomain, data])

  return (
    <div className="w-full">
      <Chart
        options={chartData.options}
        series={chartData.series}
        type="bar"
        height={350}
      />
    </div>
  )
}

export default TeamPositionChart
