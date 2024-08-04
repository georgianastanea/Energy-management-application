import React from "react";
import ReactApexChart from "react-apexcharts";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./chart-style.css";


class MeasurementsChart extends React.Component {
    constructor(props) {
      super(props);
  
      const currentDate = new Date();
      currentDate.setHours(0, 0, 0, 0);
  
      this.state = {
        selectedDate: currentDate,
        series: [
          {
            name: "Hourly Consumption",
            data: this.filterDataByDay(props.measurementsMap, currentDate),
            type: "bar", // Set type to "bar" for bars
          },
        ],
        options: {
          chart: {
            height: 350,
            type: "scatter", // Keep this as scatter
            dropShadow: {
              enabled: true,
              color: "#000",
              top: 18,
              left: 7,
              blur: 10,
              opacity: 0.2,
            },
  
            toolbar: {
              show: false,
            },
            offsetX: 20,
            offsetY: 10,
          },
          colors: ["#77B6EA"],
          dataLabels: {
            enabled: true,
            formatter: function (val) {
              return val !== undefined ? val.toFixed(2) : val;
            },
          },
          markers: {
            size: 6,
          },
          xaxis: {
            type: "datetime",
            title: {
              text: "Time",
            },
            tickAmount: 9,
            labels: {
              show: true,
              formatter: function (value) {
                return new Date(value).getHours() + ":00";
              },
            },
          },
          yaxis: {
            title: {
              text: "Consumption",
            },
            min: 0,
            max: 100,
            tickAmount: 10,
            labels: {
              formatter: function (value) {
                return value.toFixed(2);
              },
            },
          },
          legend: {
            position: "top",
            horizontalAlign: "right",
            floating: true,
            offsetY: -25,
            offsetX: -5,
          },
        },
      };
    }
  
    handleDateChange = (date) => {
      this.setState({
        selectedDate: date,
        series: [
          {
            name: "Hourly Consumption",
            data: this.filterDataByDay(this.props.measurementsMap, date),
            type: "bar", 
            color: ""
          },
        ],
      });
    };
  
    filterDataByDay(measurementsMap, selectedDate) {
      const startDate = new Date(selectedDate);
      startDate.setHours(0, 0, 0, 0);
  
      const endDate = new Date(startDate);
      endDate.setDate(endDate.getDate() + 1);
  
      console.log(measurementsMap);
  
      const measurementsArray = Object.entries(measurementsMap);
      const filteredMeasurements = measurementsArray
        .filter(([key]) => {
          const timestamp = new Date(key);
          return timestamp >= startDate && timestamp <= endDate;
        })
        .map(([key, value]) => ({
          x: new Date(key).getTime(),
          y: value,
        }));
  
      return filteredMeasurements;
    }
  
    render() {
      return (
        <div>
          <div className="div-date-picker">
            <DatePicker
              className="date-picker"
              selected={this.state.selectedDate}
              onChange={this.handleDateChange}
            />
          </div>
  
          <div id="chart">
            <ReactApexChart
              options={this.state.options}
              series={this.state.series}
              type="scatter" 
              height={350}
            />
          </div>
        </div>
      );
    }
  }
  
  export default MeasurementsChart;