import React from 'react';
import styled from "styled-components";
import { NavLink } from "react-router-dom";


const MenuLinkContainer = styled(NavLink)`

    font-size: 20px;
    cursor: pointer;
    text-decoration: none;
    padding-bottom: 2px;
    color: #222;

    /* CSS의 가상클래스 hover */
    &:hover {
        color: #22b8cf;
    }

    &:after {
        content: '|';
        display: inline-block;
        padding: 0 7px;
        color: #ccc;
    }

    &:last-child {
        &:after {
            color: #fff;
        }
    }
    &.active {
        text-decoration: underline;
        color: #22b8cf;
    }
`;

const MenuLink = ({to, children}) => <MenuLinkContainer to={to}>{children}</MenuLinkContainer>;

export default MenuLink;