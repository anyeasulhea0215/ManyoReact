import React,{memo} from "react";

import styled from "styled-components";


import Spinner from "../components/Spinner";
import MemberStaticsCount from "./MemberStaticsCount";

import PopularProductStatics from "./PopularProductStatics";

import Dashboard from "./Dashboard";



const PagesContainer=styled.div`

  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;


const Pages=memo(() => {
    return(
        <PagesContainer>

            <MemberStaticsCount />


             <PopularProductStatics />



        </PagesContainer>
    );
});

export default Pages;