package com.example.todofragment.bean;

import java.util.List;

public class GetToDoThings {
    /*String message;*/
    List<GetToDothingMessage> data;

    public GetToDoThings(List<GetToDothingMessage> data) {
        this.data = data;
    }
    /*String status;*/

    /*public GetToDoThings(String message, List<GetToDothingMessage> data, String status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/

    public List<GetToDothingMessage> getData() {
        return data;
    }

    public void setData(List<GetToDothingMessage> data) {
        this.data = data;
    }

    /*public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

    public class GetToDothingMessage {
        private String id;
        private String title;
        private String description;
        private String status;
        private String user_id;
        private String created_at;
        private String updated_at;

        public GetToDothingMessage(String id, String title, String description, String status, String user_id, String created_at, String updated_at) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.status = status;
            this.user_id = user_id;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
