import React, { memo, useEffect, useMemo } from "react";
import styled from "styled-components";
import { useSelector, useDispatch } from "react-redux";
import { getPopularProducts } from "../slices/PopularProductSlice"; //

import Spinner from "../components/Spinner";
import ErrorView from "../components/ErrorView";

import {
  Chart,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  BarElement,
} from "chart.js";
import { Bar } from "react-chartjs-2";

Chart.register(CategoryScale, LinearScale, Title, Tooltip, Legend, BarElement);

const PopularProductStaticsContainer = styled.div`
  width: 100%;
  height: 400px;
  position: relative;
`;

const PopularProductStatics = memo(() => {
  const { loading, status, message, item } = useSelector(
    (state) => state.PopularProductSlice
  );

  const dispatch = useDispatch();

useEffect(() => {
  if (!loading && !item) {
    dispatch(getPopularProducts());
  }
}, [dispatch, loading, item]);


  const { labels, values } = useMemo(() => {
    if (!item) return { labels: null, values: null };

    const labels = item.map((v) => v.productName);
    const values = item.map((v) => v.totalCount);

    return { labels, values };
  }, [item]);

  return (
    <PopularProductStaticsContainer>
       {loading && <Spinner loading={true} />}
      <ErrorView status={status} message={message} />

      {labels && values && (
        <Bar
          data={{
            labels,
            datasets: [
              {
                label: "판매 수량",
                data: values,
                backgroundColor: "rgba(255, 159, 64, 0.5)",
                borderColor: "rgba(255, 159, 64, 1)",
                borderWidth: 1,
              },
            ],
          }}
          options={{
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: { position: "bottom" },
              title: {
                display: true,
                text: "어제 기준 인기상품 순위 Top 5",
                font: { size: 18 },
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
    </PopularProductStaticsContainer>
  );
});

export default PopularProductStatics;
