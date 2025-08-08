import React,{memo, useEffect,useMemo} from "react";

import styled from "styled-components";

import { useSelector,useDispatch } from "react-redux";
import { getList } from "../slices/MemberStaticsSlice";  //Slice 객체

import Spinner from "../components/Spinner";
import ErrorView  from "../components/ErrorView";

/** chart.js 관련 */
import{
    Chart,
    CategoryScale,
    LinearScale,
    Title,
    Tooltip,
    Legend,
    //막대그래프
    BarElement,
} from "chart.js"

import {Bar} from "react-chartjs-2";

Chart.register(CategoryScale,LinearScale,Title,Tooltip,Legend,BarElement);

const MemberStaticsContainer=styled.div`
  width: 100%;
  height: 400px;
  position: relative;
`;


const MemberStatics=memo(() => {

    const {loading,status,message,item}= useSelector((state) => state.MemberStaticsSlice);

     //리덕스 디스패치 함수
    const dispatch=useDispatch();

    //컴포넌트가 마운트될 때 데이터 요청
     useEffect(() => {
    if (item && Array.isArray(item) && item.length > 0) return;
    dispatch(getList());
  }, [dispatch, item]);

  const { labels, values } = useMemo(() => {
    if (!item) return { labels: null, values: null };

    const labels = item.map((v) => v.date); // 날짜가 라벨
    const values = item.map((v) => v.count); // 가입자 수가 데이터

    return { labels, values };
  }, [item]);


    return(
        <MemberStaticsContainer>

           <Spinner loading={loading} />
      <ErrorView status={status} message={message} />

      {labels && values && (
        <Bar
          data={{
            labels,
            datasets: [
              {
                label: "가입자 수",
                data: values,
                backgroundColor: "rgba(75, 192, 192, 0.5)",
                borderColor: "rgba(75, 192, 192, 1)",
                borderWidth: 1,
              },
            ],
          }}
          options={{
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: "bottom",
              },
              title: {
                display: true,
                text: "일별 가입자 수 통계",
                font: {
                  size: 18,
                },
              },
            },
            scales: {
              y: {
                beginAtZero: true,
                ticks: {
                  stepSize: 1,
                },
              },
            },
          }}
        />
      )}

        </MemberStaticsContainer>
    );
});

export default MemberStatics;