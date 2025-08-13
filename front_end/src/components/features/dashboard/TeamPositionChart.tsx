import { useState, useEffect } from 'react'
import Chart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'

interface TeamPositionChartProps {
  selectedDomain: string
}

const TeamPositionChart = ({ selectedDomain }: TeamPositionChartProps) => {
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
      const domainData: { [key: string]: number[] } = {
        all: [176, 347, 183, 103, 68], // 전체 도메인 합계 [백엔드, 프론트엔드, AI, 디자인, PM]
        web: [75, 145, 35, 30, 25], // 웹 개발
        mobile: [48, 98, 58, 25, 18], // 모바일
        ai: [35, 48, 102, 18, 12], // AI/ML
        game: [68, 88, 25, 35, 20], // 게임
        iot: [50, 35, 48, 18, 15] // IoT
      }

      return domainData[domain] || domainData.all
    }

    const data = getDomainData(selectedDomain)
    
    setChartData(prev => ({
      ...prev,
      series: [{
        name: '인원 수',
        data: data
      }]
    }))
  }, [selectedDomain])

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
