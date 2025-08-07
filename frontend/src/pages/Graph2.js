import React,{memo, useEffect,useMemo} from "react";

import styled from "styled-components";

import { useSelector,useDispatch } from "react-redux";


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

const Graph2Container=styled.div`

`;


const Graph2=memo(() => {


    return(
        <Graph2Container>



        </Graph2Container>
    );
});

export default Graph2;