
import styled from "styled-components";

const Table = styled.table`
    border-collapse: collapse;
    border-top: 3px solid #168;
    font-size: 14px;
    text-align: center;
    margin: auto;
    width: 100%;

    th {
        color: #168;
        background: #f0f6f9;
        padding: 10px;
        border: 1px solid #ddd;

        &:first-child {
            border-left: 0;
        }

        &:last-child {
            border-right: 0;
        }
    }

    td {
        padding: 10px;
        border: 1px solid #ddd;

        &:first-child {
            border-left: 0;
        }

        &:last-child {
            border-right: 0;
        }
    }
`;

const TableView = styled(Table)`
    th {
        width: 150px;
    }

    td {
        text-align: left;
    }
`;

const TableInput = styled(TableView)`
    td {
        position: relative;

        input {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            padding: 10px;
            box-sizing: border-box;
            border: 0;
        }
    }
`;

const Buttons = styled.div`
    display: flex;
    justify-content: center;
    margin-top: 10px;

    button, a {
        padding: 10px 20px;
        font-size: 14px;
        line-height: 100%;
        cursor: pointer;
        margin: 0 5px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border: 1px solid #e0e0e0;
        border-radius: 5px;
        transition: background-color 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;

        &:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }

        &:focus {
            outline: none;
            box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
        }

        &:hover {
            background-color: #0056b3;
        }

        &:active {
            background-color: #004494;
            transform: scale(0.98);
        }
    }
`;

export { Table, TableView, TableInput, Buttons };
