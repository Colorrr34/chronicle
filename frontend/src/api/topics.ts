import { API_BASE_URL } from "./client";

export default async function getAllTopics() {
  const url = API_BASE_URL + "/api/topics";
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`Response status: ${response.status}`);
  }
  const result = await response.json();
  return result;
}
