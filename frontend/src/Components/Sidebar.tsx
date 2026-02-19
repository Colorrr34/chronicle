import { useEffect, useState } from "react";
import getAllTopics from "../api/topics";
import type { Topic } from "../types";

export default function Sidebar() {
  const [topics, setTopics] = useState<Topic[]>([]);
  useEffect(() => {
    const fetchTopics = async () => {
      const topics: Topic[] = await getAllTopics();
      setTopics(topics);
    };
    fetchTopics();
  }, []);
  return (
    <aside>
      <ul className="sidebar-list">
        {topics.map((topic) => (
          <li key={topic.topic}>{topic.topic}</li>
        ))}
      </ul>
    </aside>
  );
}
